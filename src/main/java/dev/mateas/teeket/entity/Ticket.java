package dev.mateas.teeket.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Ticket {
    @Id
    private String id;
    private String event;
}
