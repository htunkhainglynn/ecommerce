package com.project.ecommerce.service.implementation;

import com.project.ecommerce.dto.SignUpDto;
import com.project.ecommerce.dto.UserDetailDto;
import com.project.ecommerce.dto.UserDto;
import com.project.ecommerce.entitiy.Order;
import com.project.ecommerce.entitiy.Role;
import com.project.ecommerce.entitiy.User;
import com.project.ecommerce.exception.UserException;
import com.project.ecommerce.repo.UserRepository;
import com.project.ecommerce.service.DynamicQueueManager;
import com.project.ecommerce.service.UserService;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper mapper;

    private final DynamicQueueManager dynamicQueueManager;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           ModelMapper mapper,
                           DynamicQueueManager dynamicQueueManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
        this.dynamicQueueManager = dynamicQueueManager;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional(readOnly = true)
    @Override
    public Page<UserDto> getAllUsers(String keyword, Optional<Integer> page, Optional<Integer> size) {
        PageRequest pageRequest = PageRequest.of(
                page.orElse(0),
                size.orElse(10));

        Specification<User> specification = (root, query, cb) -> cb.conjunction();

        Predicate<String> isDouble = s -> {
            try {
                Double.parseDouble(s);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        };

        specification = StringUtils.hasLength(keyword) && !isDouble.test(keyword)
                ? specification.and((root, query, cb) ->
                cb.or(
                        cb.like(cb.lower(root.get("username")), "%" + keyword.toLowerCase() + "%"),
                        cb.like(cb.lower(root.get("email")), "%" + keyword.toLowerCase() + "%"),
                        cb.like(root.get("phoneNumber"), "%" + keyword + "%")
                ))
                : specification;

        specification = StringUtils.hasLength(keyword) && isDouble.test(keyword)
                ? specification.and((root, query, cb) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Order> subRoot = subquery.from(Order.class);

            Expression<Long> userIdExpression = subRoot.get("user").get("id");
            Expression<Double> totalSpentExpression = cb.sum(subRoot.get("totalPrice"));

            subquery.select(userIdExpression)
                    .groupBy(userIdExpression)
                    .having(cb.greaterThanOrEqualTo(totalSpentExpression, Double.parseDouble(keyword)));

            return cb.in(root.get("id")).value(subquery);
        })
                : specification;

        return userRepository.findAll(specification, pageRequest)
                .map(UserDto::new);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UserDetailDto> getUserById(Long id) {
        return userRepository.findById(id).map(UserDetailDto::new);
    }

    @Override
    public User saveUser(SignUpDto signUpDto) {
        Optional<User> optionalUser = userRepository.getReferenceByUsername(signUpDto.getUsername());
        if (optionalUser.isPresent()) {
            throw new UserException("User already exists");
        }
        signUpDto.setActive(true);
        signUpDto.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        // default
        signUpDto.setRoles(List.of(Role.USER));
        User user = userRepository.save(mapper.map(signUpDto, User.class));

        // create queue for user
        dynamicQueueManager.createQueueForUser(signUpDto.getUsername());
        return user;
    }

    @Override
    public UserDetailDto updateUser(UserDetailDto userDetailDto) {
        User originalUser = userRepository.getReferenceById(userDetailDto.getId());
        User user = mapper.map(userDetailDto, User.class);
        user.setPassword(originalUser.getPassword());
        user.setAddresses(originalUser.getAddresses());
        return mapper.map(userRepository.save(user), UserDetailDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UserDetailDto> getUserByUsername(String username) {
        return userRepository.findByUsername(username).map(UserDetailDto::new);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Long getRoleCount() {
       return userRepository.getRoleCount();
    }

}
