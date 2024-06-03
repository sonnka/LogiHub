package com.logihub.service;

import com.logihub.exception.MailException;

public interface MailService {

    void sendNewAdminMessage(String receiverEmail) throws MailException;

    void sendNotApprovedAdminMessage(String receiverEmail) throws MailException;

    void sendApprovedAdminMessage(String receiverEmail, String tempPassword) throws MailException;
}
