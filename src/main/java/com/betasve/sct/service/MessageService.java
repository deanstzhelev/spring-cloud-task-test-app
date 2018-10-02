package com.betasve.sct.service;

import com.betasve.sct.model.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageService extends CrudRepository<Message, Long> { }
