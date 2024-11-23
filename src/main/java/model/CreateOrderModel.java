package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreateOrderModel {
    private String firstName;
    private String lastName;
    private String address;
    private int metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    List<String> color;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new Random();

    public CreateOrderModel(List color){
        this.firstName = generateRandomString(10);
        this.lastName = generateRandomString(10);
        this.address = generateRandomString(10);
        this.metroStation = (int) (Math.random() * 10);
        this.phone = generateRandomString(10);
        this.rentTime = (int) (Math.random() * 10);
        this.deliveryDate = LocalDate.now().toString();
        this.comment = generateRandomString(10);
        this.color = color;
    }
    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
}
