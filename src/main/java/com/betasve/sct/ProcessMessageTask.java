package com.betasve.sct;

import com.betasve.sct.model.Message;
import com.betasve.sct.service.MessageService;
import com.betasve.sct.utils.CmdMessageInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.cloud.task.repository.TaskRepository;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableTask
public class ProcessMessageTask{
	@Bean
	public CommandLineRunner commandLineRunner() {
		return new ProcessMessageCommandLineRunner();
	}

	public static void main(String[] args) {
		SpringApplication.run(ProcessMessageTask.class, args);
	}

	public static class ProcessMessageCommandLineRunner implements CommandLineRunner {
		@Autowired
		private MessageService messageService;

		@Autowired
		private TaskRepository taskRepository;

		@Override
		public void run(String... strings) {
			CmdMessageInput input = new CmdMessageInput(strings);
			if (input.valid()) {
				messageService.save(new Message(input.getType(), input.getPayload()));
			} else {
				System.out.println("The following problems occurred:");
				input.getErrors().keySet().stream().forEach(
					e -> System.out.println(e + ": " + input.getErrors().get(e))
				);
			}
		}
	}
}
