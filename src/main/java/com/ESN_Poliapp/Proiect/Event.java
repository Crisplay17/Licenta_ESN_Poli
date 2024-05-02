package com.ESN_Poliapp.Proiect;
import jakarta.persistence.*;
import org.apache.catalina.User;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

import java.util.*;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String eventName;

    @Column(nullable = false)
    private Date eventDate;

    @Column(nullable = false)
    private String eventLocation;

    private String eventCause;

    private int eventParticipants;

    @Column(columnDefinition = "TEXT")
    private String eventDescription;

    private String eventGoal;

    private String eventOrganizer; // Adăugăm câmpul pentru organizatorul evenimentului
    private String eventLink; // Adăugăm câmpul pentru link-ul evenimentului
    @ManyToMany(mappedBy = "events")
    private List<VolunteerUser> participants = new ArrayList<>();


    @Column(name = "background_image")
    private byte[] backgroundImage;

    private Boolean joined;


    public Event() {
        // Constructor fără argumente
    }

    public Event(String eventName, Date eventDate, String eventLocation, String eventCause, int eventParticipants, String eventDescription, String eventGoal) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.eventCause = eventCause;
        this.eventDescription = eventDescription;
        this.eventParticipants = eventParticipants;
        this.eventGoal = eventGoal;
    }

    // Getteri și setteri pentru toate atributele

    public Long getId() { return id; }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventCause() {
        return eventCause;
    }

    public void setEventCause(String eventCause) {
        this.eventCause = eventCause;
    }

    public int getEventParticipants() {
        return eventParticipants;
    }

    public void setEventParticipants(int eventParticipants) {
        this.eventParticipants = eventParticipants;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventGoal() {
        return eventGoal;
    }

    public void setEventGoal(String eventGoal) {
        this.eventGoal = eventGoal;
    }

    public List<VolunteerUser> getParticipants() {
        return participants;
    }

    public void setParticipants(List<VolunteerUser> participants) {
        this.participants = participants;
    }
    // Getteri și setteri pentru eventOrganizer și eventLink
    public String getEventOrganizer() {
        return eventOrganizer;
    }

    public void setEventOrganizer(String eventOrganizer) {
        this.eventOrganizer = eventOrganizer;
    }

    public String getEventLink() {
        return eventLink;
    }

    public void setEventLink(String eventLink) {
        this.eventLink = eventLink;
    }

    public boolean isJoined() {
        return joined != null && joined;
    }


    public void setJoined(boolean joined) {
        this.joined = joined;
    }

    // Alți getteri și setteri pentru atributele suplimentare, dacă este cazul
    public byte[] getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(byte[] backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

}

