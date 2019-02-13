package com.ostaszewski.article_sharing_app.service;

import com.ostaszewski.article_sharing_app.config.Constants;
import com.ostaszewski.article_sharing_app.domain.*;
import com.ostaszewski.article_sharing_app.repository.*;
import com.ostaszewski.article_sharing_app.repository.search.UserSearchRepository;
import com.ostaszewski.article_sharing_app.security.AuthoritiesConstants;
import com.ostaszewski.article_sharing_app.security.SecurityUtils;
import com.ostaszewski.article_sharing_app.service.dto.InterestDTO;
import com.ostaszewski.article_sharing_app.service.dto.MessageDTO;
import com.ostaszewski.article_sharing_app.service.dto.UserDTO;
import com.ostaszewski.article_sharing_app.service.dto.UserMessageDTO;
import com.ostaszewski.article_sharing_app.service.mapper.InterestMapper;
import com.ostaszewski.article_sharing_app.service.mapper.MessageMapper;
import com.ostaszewski.article_sharing_app.service.mapper.UserMapper;
import com.ostaszewski.article_sharing_app.service.util.ImageUtil;
import com.ostaszewski.article_sharing_app.service.util.RandomUtil;
import com.ostaszewski.article_sharing_app.web.rest.errors.EntityNotFoundException;
import com.ostaszewski.article_sharing_app.web.rest.errors.InvalidPasswordException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class UserService {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserSearchRepository userSearchRepository;

    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;

    private final UserMapper userMapper;

    private final InviteRepository inviteRepository;

    private final InviteStrangersRepository inviteStrangersRepository;

    private final MessageMapper messageMapper;

    private final MessageRepository messageRepository;

    private final InterestMapper interestMapper;

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                userSearchRepository.save(user);
                this.clearUserCaches(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);

        return userRepository.findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                this.clearUserCaches(user);
                userRepository.save(user);
                return user;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmailIgnoreCase(mail)
            .filter(User::isActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                this.clearUserCaches(user);
                return user;
            });
    }

    public User registerUser(UserDTO userDTO, String password) {

        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setEmail(userDTO.getEmail());
        newUser.setAvatar(ImageUtil.prepareImage(userDTO.getAvatar()));
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        Set<User> friends = new HashSet<>();
        userRepository.findOneByLogin("admin").ifPresent(friends::add);
        newUser.setFriends(friends);
        newUser.setInterests(userDTO.getInterests());
        userRepository.save(newUser);
        userSearchRepository.save(newUser);
        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setAvatar(ImageUtil.prepareImage(userDTO.getAvatar()));
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO.getAuthorities().stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        userRepository.save(user);
        userSearchRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }


    public void updateUser(String firstName, String lastName, String email, String langKey, String avatar) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email);
                user.setLangKey(langKey);
                user.setAvatar(ImageUtil.prepareImage(avatar));
                userSearchRepository.save(user);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
            });
    }


    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository
            .findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                this.clearUserCaches(user);
                user.setLogin(userDTO.getLogin());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                user.setEmail(userDTO.getEmail());
                user.setAvatar(userDTO.getAvatar());
//                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO.getAuthorities().stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);
                userSearchRepository.save(user);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(UserDTO::new);
    }

    public void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> {
            userRepository.delete(user);
            userSearchRepository.delete(user);
            this.clearUserCaches(user);
            log.debug("Deleted User: {}", user);
        });
    }

    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                this.clearUserCaches(user);
                userRepository.save(user);
                log.debug("Changed password for User: {}", user);
            });
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(Long id) {
        return userRepository.findOneWithAuthoritiesById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllFriends() {
        return userMapper.usersToUserDTOs(new ArrayList<>(
            userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).get().getFriends()));
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getStrangers() {
        List<User> strangers = userRepository.findAll();
        if (SecurityUtils.getCurrentUserLogin().isPresent() && userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).isPresent()) {
            User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).get();
            strangers.removeAll(user.getFriends());
        }
        return userMapper.usersToUserDTOs(strangers);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS));
        for (User user : users) {
            log.debug("Deleting not activated user {}", user.getLogin());
            userRepository.delete(user);
            userSearchRepository.delete(user);
            this.clearUserCaches(user);
        }
    }


    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Users for query {}", query);
        return userSearchRepository.search(queryStringQuery(query), pageable)
            .map(userMapper::userToUserDTO);
    }


    public void inviteUser(Long id) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).ifPresent(user -> {
            user.getInvites().add(inviteRepository.save(new Invite(userRepository.getOne(id))));
            userRepository.save(user);
        });
    }

    public void acceptInvite(Long id) {
        log.debug("Request to accept invite from user with id: ", id);
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).ifPresent(user -> {
            User friend = userRepository.getOne(id);
            user.getFriends().add(friend);
            friend.getFriends().add(user);
            user.getInvitesFromStrangers().remove(user.getInvitesFromStrangers().stream().filter(i -> i.getFriend().equals(friend)).findFirst().get());
            friend.getInvites().remove(friend.getInvites().stream().filter(i -> i.getFriend().equals(user)).findFirst().get());
            userRepository.save(user);
            userRepository.save(friend);
        });
    }

    public void handleInviteFromStranger(Long id) {
        log.debug("Request to handle invite from user with id: ", id);
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).ifPresent(user -> {
            User stranger = userRepository.getOne(id);
            stranger.getInvitesFromStrangers().add(inviteStrangersRepository.save(new InvitesFromStrangers(user)));
            userRepository.save(stranger);
        });
    }


    public void deleteUserFromFriends(Long id) {
        log.debug("Request to delete friend with id: ", id);
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).ifPresent(user -> {
            User friend = userRepository.getOne(id);
            userRepository.save(user.removeFriend(friend));
            userRepository.save(friend.removeFriend(user));
        });
    }

    public MessageDTO saveMessage(com.ostaszewski.article_sharing_app.service.dto.MessageDTO messageDTO) {
        log.debug("Request to save message with id: ", messageDTO.getId());
        Message message = messageMapper.toEntity(messageDTO);
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).ifPresent(user -> {
            message.setCreationDate(Instant.now());
            messageRepository.save(message);
            userRepository.save(user.addMessages(message));
        });
        return messageMapper.toDto(message);
    }

    public List<UserMessageDTO> getAllUserMessages(Long receiverId) {
        log.debug("Request to get all user messages");
        List<UserMessageDTO> conversation = new ArrayList<>();
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).ifPresent(user -> {
            Long senderId = user.getId();
            List<MessageDTO> loggedUserMessages = userRepository.findUserMessages(senderId, receiverId);
            List<MessageDTO> receiverMessages = userRepository.findUserMessages(receiverId, senderId);
            for (MessageDTO messageDTO : loggedUserMessages) {
                conversation.add(new UserMessageDTO(messageDTO.getId(), messageDTO.getContent(), senderId, messageDTO.getCreationDate(), messageDTO.getRead()));
            }
            for (MessageDTO messageDTO2 : receiverMessages) {
                conversation.add(new UserMessageDTO(messageDTO2.getId(), messageDTO2.getContent(), receiverId, messageDTO2.getCreationDate(), messageDTO2.getRead()));
            }
            Collections.sort(conversation);
        });
        return conversation;
    }

    public List<InterestDTO> getUserInterests() {
        List<InterestDTO> interests = interestMapper.toDto(new ArrayList<>(userRepository.findOneByLogin(
            SecurityUtils.getCurrentUserLogin().get()).get().getInterests()));
        if (Objects.nonNull(interests))
            return interests;
        else return new ArrayList<InterestDTO>();
    }

    public List<UserDTO> getAllUsers() {
        return userMapper.usersToUserDTOs(userRepository.findAll());
    }

    public Integer getUserNotifications() {
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).orElseThrow(() -> new EntityNotFoundException("No such user"));
        return user.getInvitesFromStrangers().size();
    }

}
