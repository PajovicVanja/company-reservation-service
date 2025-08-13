package org.soa.companyService.dto;

public class CreateSmsNotificationConfigRequest {
    private String name;
    private String notificationMessage;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNotificationMessage() { return notificationMessage; }
    public void setNotificationMessage(String notificationMessage) { this.notificationMessage = notificationMessage; }
}
