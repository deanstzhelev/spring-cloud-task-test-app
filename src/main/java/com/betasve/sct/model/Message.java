package com.betasve.sct.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Message implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String msgtype;

    @Column(nullable = false)
    private String payload;

    @Column(nullable = false, name = "created_at", updatable = false)
    @CreationTimestamp
    private Date createdAt = new Date();

    public Message() {
    }

    public Message(String msgtype, String payload) {
        this.msgtype = msgtype;
        this.payload = payload;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public String getPayload() {
        return payload;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "type: " + getMsgtype() + ", \npayload: " + getPayload()
                + ", \ncreatedAt: " + getCreatedAt();
    }
}
