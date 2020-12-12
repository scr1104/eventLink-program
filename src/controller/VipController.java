package controller;

import Presenter.AttendeePresenter;
import Presenter.InitialPresenter;
import Presenter.UserPresenter;
import Presenter.VipPresenter;
import use_case.*;

import java.io.IOException;
import java.util.Scanner;


public class VipController {

    UserController userController;
    MessageController messageController;
    LogoutController logoutController;
    InitialPresenter initialPresenter;
    VipManager vipManager;
    VipPresenter vipPresenter;
    UserPresenter userPresenter;
    EventManager eventManager;
    MessageManager messageManager;
    UserManager userManager;
    OrganizerManager organizerManager;
    SpeakerManager speakerManager;
    TicketController ticketController;
    AttendeeController attendeeController;
    AttendeePresenter attendeePresenter;

    public VipController(UserController uc, MessageController mc, LogoutController lc, InitialPresenter ip,
                         VipManager vm, VipPresenter vp, UserPresenter up, EventManager em, MessageManager mm,
                         UserManager um, OrganizerManager om, SpeakerManager sm, TicketController tc,
                         AttendeeController ac, AttendeePresenter ap){
        userController = uc;
        messageController = mc;
        logoutController = lc;
        initialPresenter = ip;
        vipManager = vm;
        vipPresenter = vp;
        userPresenter = up;
        eventManager = em;
        messageManager = mm;
        userManager = um;
        organizerManager = om;
        speakerManager = sm;
        ticketController = tc;
        attendeeController = ac;
        attendeePresenter = ap;

    }

    /**
     * Main menu for Vip, where Vip can: browse all events, manage events Vip is enrolled in,
     * go to message menu, see friends and their events, or logout.
     */
    public boolean mainVipControl() throws IOException {
        System.out.println(vipPresenter.mainDisplay());
        String choice;
        Scanner in = new Scanner(System.in);
        choice = in.nextLine();
        switch (choice) {
            case "1":
                return browseEventsControl();
            case "2":
                userController.seeSchedule();
                return mainVipControl();
            case "3":
                String eventName = userController.chooseEventControl();
                if (eventName == null){
                    System.out.println("Please choose from the given options.");
                    return mainVipControl();
                }
                return myEventControl(eventName);
            case "4":
                return messageControl();
            case "5":
                String friend = userController.seeMyFriends(userManager.getCurrentUserId());
                if (friend == null){
                    System.out.println(vipPresenter.noFriendsDisplay());
                    return mainVipControl();
                }
                if (friend.equals("empty")){
                    System.out.println(vipPresenter.toMainMenuDisplay());
                    return mainVipControl();
                }
                attendeeController.seeMyFriendsEvents(friend);
                return mainVipControl();
            case "6":
                return logoutController.logout();
            default:
                System.out.println(initialPresenter.OutOfChoice());
                // repeat this method again if the choice was not made properly.
                return mainVipControl();
        }
    }


    /**
     * main controller for VIP user to browse events
     * @throws IOException
     */
    public boolean browseEventsControl() throws IOException {
        System.out.println(vipPresenter.eventTypeDisplay());
        String choice;
        Scanner in = new Scanner(System.in);

        choice = in.nextLine();
        switch (choice) {
            case "1":
                return browseVipEventsControl();
            case "2":
                return browseRegularEventsControl();
            case "3":
                return mainVipControl();
            default:
                System.out.println(initialPresenter.OutOfChoice());
                // repeat this method again if the choice was not made properly.
                return browseEventsControl();
        }
    }

    /**
     * Menu for browsing all events. Makes sure input from Attendee is valid and depending on the enrollment status
     * of the event the Attendee chose to browse, they will have different options to manage the event.
     */
    public boolean browseVipEventsControl() throws IOException {
        System.out.println(vipPresenter.vipEventsDisplay());
        String choice;
        Scanner in = new Scanner(System.in);

        choice = in.nextLine();
        String eventName;
        if (choice.isEmpty()) {
            System.out.println(vipPresenter.toMainMenuDisplay());
            return mainVipControl();
        }
        int choiceInt = Integer.parseInt(choice);
        if (choiceInt <= eventManager.getVipEventNames().size()) {
            eventName = eventManager.getVipEventNames().get(choiceInt - 1);
            if (vipManager.isEnrolled(eventName)) {
                return myEventControl(eventName);
            } else {
                return newEventControl(eventName);
            }
        }
        System.out.println(initialPresenter.OutOfChoice());
        return browseVipEventsControl();
    }


    /**
     * Menu for browsing all events. Makes sure input from Attendee is valid and depending on the enrollment status
     * of the event the Attendee chose to browse, they will have different options to manage the event.
     */
    public boolean browseRegularEventsControl() throws IOException {
        System.out.println(attendeePresenter.allEventsDisplay());
        String choice;
        Scanner in = new Scanner(System.in);

        choice = in.nextLine();
        String eventName;
        if (choice.isEmpty()) {
            System.out.println(vipPresenter.toMainMenuDisplay());
            return mainVipControl();
        }
        int choiceInt = Integer.parseInt(choice);
        if (choiceInt <= eventManager.getAttendeeEventNames().size()) {
            eventName = eventManager.getAttendeeEventNames().get(choiceInt - 1);
            if (vipManager.isEnrolled(eventName)) {
                return myEventControl(eventName);
            } else {
                return newEventControl(eventName);
            }
        }
        System.out.println(initialPresenter.OutOfChoice());
        return browseRegularEventsControl();
    }


    /**
     * Menu for managing the specific event Vip is enrolled in. Vip can cancel enrollment, see all
     * users attending the event, go back to previous menu, go back to main menu, or logout.
     * @param eventName The name of the event that the AttenVipdee wants to manage.
     */
    public boolean myEventControl(String eventName) throws IOException {
        System.out.println(vipPresenter.myEventDisplay());
        String choice;
        Scanner in = new Scanner(System.in);
        choice = in.nextLine();
        switch (choice) {
            case "1":
                userManager.removeFromEventList(eventName);
                eventManager.removeFromAttendeeList(userManager.getCurrentUserId(), eventName);
                System.out.println(vipPresenter.cancelledEventDisplay());
                return mainVipControl();
            case "2":
                userController.seeAllUsersAndAddFriend(eventName);
                return myEventControl(eventName);
            case "3" :
                ticketController.getTicket(eventName);
                return myEventControl(eventName);
            case "4":
                return browseEventsControl();
            case "5":
                return mainVipControl();
            case "6":
                return chooseSpeakerControl(eventName);
            case "7":
                return logoutController.logout();
            default:
                System.out.println(initialPresenter.OutOfChoice());
                return myEventControl(eventName);
        }
    }



    /**
     * Menu for managing the specific event Vip is not enrolled in. Vip can enroll in the event, or go back
     * to main menu, or logout.
     * @param eventName The name of the event that the Vip wants to manage.
     */
    public boolean newEventControl(String eventName) throws IOException {
        System.out.println(vipPresenter.newEventDisplay());
        String choice;
        Scanner in = new Scanner(System.in);
        choice = in.nextLine();
        switch (choice) {
            case "1":
                if(!eventManager.eventIsFull(eventName)){
                    userManager.addToEventList(eventName);
                    eventManager.addToAttendeeList(userManager.getCurrentUserId(), eventName);
                    System.out.println(vipPresenter.enrolledEventDisplay());
                }
                else{
                    System.out.println(vipPresenter.roomFullDisplay());
                }
                return mainVipControl();
            case "2":
                return mainVipControl();
            case "3":
                return logoutController.logout();
            default:
                System.out.println(initialPresenter.OutOfChoice());
                return myEventControl(eventName);
        }

    }


    /**
     * Menu for deciding which of the speakers of this specified event to privately message.
     * @param eventName A String representing the name of the specified Event.
     */
    public boolean chooseSpeakerControl(String eventName) throws IOException {
        System.out.println(vipPresenter.speakersDisplay(eventName));

        String choice;
        String speakerName;
        Scanner in = new Scanner(System.in);

        choice = in.nextLine();
        if (choice.isEmpty()) {
            System.out.println(vipPresenter.toEventMenuDisplay());
            return myEventControl(eventName);
        }
        int choiceInt = Integer.parseInt(choice);
        if (choiceInt <= eventManager.getSpeakersOfEvent(eventName).size()) {
            speakerName = eventManager.getSpeakersOfEvent(eventName).get(choiceInt - 1);
            return speakerMessageControl(speakerName, eventName);
        }
        System.out.println(initialPresenter.OutOfChoice());
        return chooseSpeakerControl(eventName);
    }

    /**
     * Receives the contents of the message to be sent to the specified speaker of the specified event.
     * @param speakerName A String representing the specified speaker
     * @param eventName A String representing the specified event.
     */
    public boolean speakerMessageControl(String speakerName, String eventName) throws IOException {
        System.out.println(vipPresenter.speakerMessageContentDisplay());
        Scanner in = new Scanner(System.in);
        String content = in.nextLine();
        String speakerId = userManager.userNameToId(speakerName);
        messageManager.sendPrivateMessage(userManager.getCurrentUserId(), speakerId, content);
        System.out.println(userPresenter.messageDisplay("messageSent"));
        return myEventControl(eventName);
    }

    /**
     * Selects from different cases based on user's choice.
     * Options include displaying all chats, sending private message, going back to the main menu, and logging out.
     * @return privateMessage() or messageControl() to go back to the options display / return another method
     * (mainVipControl(), logoutController.logout()) to process selected option.
     */
    public boolean messageControl() throws IOException {
        System.out.println(vipPresenter.messageMenuDisplay());
        String choice;
        Scanner in = new Scanner(System.in);
        choice = in.nextLine();
        switch (choice) {
            case "1":
                messageController.allChatMessage();
                return messageControl();
            case "2":
                messageController.privateMessage();
                return messageControl();
            case "3":
                messageController.viewArchivedReceiver();
                return messageControl();
            case "4":
                return mainVipControl();
            case "5":
                return logoutController.logout();
            default:
                System.out.println(initialPresenter.OutOfChoice());
                return messageControl();
        }
    }

}
