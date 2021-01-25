package com.royal.app.services.impl;

import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.History;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.utils.IOUtils;
import org.springframework.stereotype.Service;

import com.royal.app.util.ResourcesLocation;

@Service
public class ChatbotUtilService {
	private static final boolean TRACE_MODE = false;
	static String botName = "super";

	public String getBotResponseforRest(String userReq) {
		//RestServiceUtil restUtil = new RestServiceUtil();
		String response = null;
		try {
			Chat chatSession = createChatSession();
			if ((userReq == null) || (userReq.length() < 1))
				userReq = MagicStrings.null_input;
			if (userReq.equals("quit")) {
				System.exit(0);
			} else {
				String request = userReq;
				response = chatSession.multisentenceRespond(request);
				while (response.contains("&lt;"))
					response = response.replace("&lt;", "<");
				while (response.contains("&gt;"))
					response = response.replace("&gt;", ">");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@SuppressWarnings("rawtypes")
	public String getBotResponseforConsole() {
		String response = null;
		try {

			Chat chatSession = createChatSession();
			String textLine = "";

			while (true) {
				System.out.print("Human : ");
				textLine = IOUtils.readInputTextLine();
				if ((textLine == null) || (textLine.length() < 1))
					textLine = MagicStrings.null_input;
				if (textLine.equals("quit")) {
					System.exit(0);
				} else {
					String request = textLine;
					if (MagicBooleans.trace_mode)
						System.out.println(
								"STATE=" + request + ":THAT=" + ((History) chatSession.thatHistory.get(0)).get(0)
										+ ":TOPIC=" + chatSession.predicates.get("topic"));
					response = chatSession.multisentenceRespond(request);
					while (response.contains("&lt;"))
						response = response.replace("&lt;", "<");
					while (response.contains("&gt;"))
						response = response.replace("&gt;", ">");
					System.out.println("Robot : " + response);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public Chat createChatSession() {
		ResourcesLocation resource = new ResourcesLocation();
		String resourcesPath = resource.getResourcesPath();
		MagicBooleans.trace_mode = TRACE_MODE;
		Bot bot = new Bot("super", resourcesPath);
		//bot.writeAIMLFiles(); // Read any new aiml files everytime
		Chat chatSession = new Chat(bot);
		bot.brain.nodeStats();
		return chatSession;
	}
}