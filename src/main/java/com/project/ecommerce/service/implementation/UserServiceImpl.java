package com.project.ecommerce.service.implementation;

import com.project.ecommerce.dto.SignUpDto;
import com.project.ecommerce.dto.UserDetailDto;
import com.project.ecommerce.dto.UserDto;
import com.project.ecommerce.entitiy.Order;
import com.project.ecommerce.entitiy.User;
import com.project.ecommerce.exception.UserException;
import com.project.ecommerce.repo.UserRepository;
import com.project.ecommerce.service.DynamicQueueManager;
import com.project.ecommerce.service.UserService;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.function.Predicate;

@Service
@Slf4j
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

        if (StringUtils.hasLength(keyword) && !isDouble.test(keyword)) {
            log.info("keyword: {}", 0);
            specification = specification.and((root, query, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("username")), "%" + keyword.toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("email")), "%" + keyword.toLowerCase() + "%"),
                            cb.like(root.get("phoneNumber"), "%" + keyword + "%")
                    )
            );
        }

        if (StringUtils.hasLength(keyword) && isDouble.test(keyword)) {
            specification = specification.and((root, query, cb) -> {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<Order> subRoot = subquery.from(Order.class);

                Expression<Long> userIdExpression = subRoot.get("user").get("id");
                Expression<Double> totalSpentExpression = cb.sum(subRoot.get("totalPrice"));

                subquery.select(userIdExpression)
                        .groupBy(userIdExpression)
                        .having(cb.greaterThanOrEqualTo(totalSpentExpression, Double.parseDouble(keyword)));

                return cb.in(root.get("id")).value(subquery);
            });

        }

        return userRepository.findAll(specification, pageRequest)
                .map(UserDto::new);
    }

    @Override
    public Optional<UserDetailDto> getUserById(Long id) {
        return userRepository.findById(id).map(UserDetailDto::new);
    }

    @Transactional
    @Override
    public void saveUser(SignUpDto signUpDto) {
        Optional<User> optionalUser = userRepository.getReferenceByUsername(signUpDto.getUsername());
        if (optionalUser.isPresent()) {
            throw new UserException("User already exists");
        }
        signUpDto.setActive(true);
        signUpDto.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        userRepository.save(mapper.map(signUpDto, User.class));

        // create queue for user
        dynamicQueueManager.createQueueForUser(signUpDto.getUsername());
    }

    @Transactional
    @Override
    public UserDetailDto updateUser(UserDetailDto userDetailDto) {
        return userRepository.findById(userDetailDto.getId())
                .map(user -> {
                    mapper.map(userDetailDto, user);
                    return mapper.map(userRepository.save(user), UserDetailDto.class);
                })
                .orElseThrow(() -> new UserException("User not found"));
    }

}
