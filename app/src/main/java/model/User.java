package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class User {

    enum Gender {FEMALE, MALE, OTHER};
    enum SleepTime {EARLY, AVERAGE, LATE};

    public String id;
    public String firstName;
    public String lastName;
    public String email;
    public Date birthDate;
    public Gender gender;
    public String originCountry;
    public String spokenLanguages;
    public Date createdAt;
    public int minBudget;
    public int maxBudget;
    public Date arrival;
    public Date departure;
    public boolean smoker;
    public boolean likesPets;
    public SleepTime sleepTime;

    public ArrayList<Hobby> hobbies;

}
