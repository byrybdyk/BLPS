package com.zarubovandlevchenko.lb1.controller;

import com.zarubovandlevchenko.lb1.dto.StatusUpdateRequest;
import com.zarubovandlevchenko.lb1.model.dbcard.Card;
import com.zarubovandlevchenko.lb1.model.dbuser.UserModal;
import com.zarubovandlevchenko.lb1.repository.dbcard.CardRepository;
import com.zarubovandlevchenko.lb1.repository.dbuser.UserRepository;
import com.zarubovandlevchenko.lb1.service.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/registration-requests")
public class SecurityAdminController {
    private final SecurityAdminService securityAdminService;
    private final NewUsersStorageService newUsersStorageService;
    private final UserService userService;



    @Autowired
    private UserRepository userRepository;

    @PersistenceContext(unitName = "usersDbEntityManagerFactory")
    private EntityManager entityManager;
    @Autowired
    private CardRepository cardRepository;

    @GetMapping
    public Map<Long, UserModal> getRequests() {
        return newUsersStorageService.getRegistrationRequests();
    }

    @PatchMapping("/{requestId}")
    public String updateStatus(@PathVariable Long requestId, @RequestBody StatusUpdateRequest request) throws Exception {
        return securityAdminService.updateRequestStatus(requestId, request.getStatus());
    }

    @PostMapping("/tessst")
    @Transactional()
    public void testSaveUser() {

//        Card card = new Card();
//        card.setUser(2L);
//        card.setCardNumber("55555555555555555557");
//        card.setCvv("123");
//        card.setNotify(true);
//        card.setPin("1234");
//        card.setExpiredAt(LocalDate.now().plusDays(1));
//        card.setIsBlocked(true);
//        card.setIsFreeze(true);
//        card.setBalance(22.2);
//        cardRepository.save(card);
//        Card savedCard = cardRepository.findByCardId(card.getCardId());
//        System.out.println(savedCard);
        UserModal user = new UserModal();
        user.setPhoneNumber("+79603333333");
        user.setLogin("testuser");
        user.setPassword("testpass");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPassportNumber("4321 223456");
        userRepository.save(user);

        System.out.println("Test saved user: " + user);
    }
}
