package model;

import model.server.UserProfile;

import java.util.Date;

public class PaintBoard {
    int[][] data = new int[50][50];
    UserProfile createdBy;
    Date updatedAt;
}
