package de.bitnoise.sonferenz.service.v2.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.Subscribe;

import de.bitnoise.sonferenz.service.v2.actions.ContentReplacement;
import de.bitnoise.sonferenz.service.v2.events.ConfigReload;
import de.bitnoise.sonferenz.service.v2.exceptions.ValidationException;
import de.bitnoise.sonferenz.service.v2.services.ConfigurationService;
import de.bitnoise.sonferenz.service.v2.services.MailService;

@Service
public class MailServiceImpl implements MailService
{
  @Autowired
  ConfigurationService config;

  // set in initMail()
  String baseUrl;

  // set in initMail()
  JavaMailSenderImpl sender;

  String from;
  

  boolean initialized;

  private String replyTo;

  public void initMail()
  {
    if (initialized)
    {
      return;
    }
    JavaMailSenderImpl tmp = new JavaMailSenderImpl();
    tmp.setHost(config.getStringValue("smtp.host"));
    if (config.isAvaiable("smtp.username"))
    {
      tmp.setUsername(config.getStringValue("smtp.username"));
      tmp.setPassword(config.getStringValue("smtp.password"));
    }

    baseUrl = config.getStringValue("baseUrl");

    sender = tmp;
    from = config.getStringValue("mail.create.from");
    replyTo = config.getStringValue("mail.create.replyTo");
  }
  
  @Subscribe
  public void onConfigReload(ConfigReload event)
  {
    initialized=false;
    initMail();
  }

  @Override
  public void sendMessage(ContentReplacement params, SimpleMailMessage msgToSend)
  {
    initMail();
    SimpleMailMessage msg = new SimpleMailMessage(msgToSend);
    String body = msg.getText();
    body = body.replace("${url.base}", baseUrl);
    body = params.process(body);
    msg.setText(body);
    msg.setFrom(from);
    msg.setReplyTo(replyTo);
    
    try
    {
      sender.send(msg);
    }
    catch (MailSendException t)
    {
      throw new ValidationException("Error while sending the mail Mail. "
          + t.getMessage());
    }
  }

  @Override
  public void sendMessage(SimpleMailMessage msgToSend)
  {
    initMail();
    SimpleMailMessage msg = new SimpleMailMessage(msgToSend);
    String body = msg.getText();
    body = body.replace("${url.base}", baseUrl);
    msg.setText(body);
    msg.setFrom(from);
    msg.setReplyTo(replyTo);
    
    try
    {
      sender.send(msg);
    }
    catch (MailSendException t)
    {
      throw new ValidationException("Error while sending the mail Mail. "
          + t.getMessage());
    }
  }

}
