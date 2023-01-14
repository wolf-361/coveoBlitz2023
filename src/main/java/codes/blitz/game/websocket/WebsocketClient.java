package codes.blitz.game.websocket;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import codes.blitz.game.bot.Bot;
import codes.blitz.game.message.bot.BotCommandMessage;
import codes.blitz.game.message.bot.BotRegistrationWithToken;
import codes.blitz.game.message.bot.BotRegistrationWithoutToken;
import codes.blitz.game.message.game.GameMessage;
import codes.blitz.game.message.game.commands.Command;
import codes.blitz.game.message.serialization.MessageDecoder;
import codes.blitz.game.message.serialization.MessageEncoder;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.CloseReason;
import jakarta.websocket.EncodeException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;

@ClientEndpoint(decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class WebsocketClient
{
    private static final String TOKEN_KEY = "TOKEN";

    private final Bot bot;
    private final CountDownLatch latch;

    public WebsocketClient(CountDownLatch latch)
    {
        this.latch = Objects.requireNonNull(latch);
        this.bot = new Bot();
    }

    @OnOpen
    public void onOpen(Session session) throws IOException, EncodeException
    {
        Record message;
        if (System.getenv(TOKEN_KEY) != null) {
            String token = System.getenv(TOKEN_KEY);
            message = new BotRegistrationWithToken(token);
        } else {
            message = new BotRegistrationWithoutToken("myJavaBot-" + UUID.randomUUID());
        }

        session.getBasicRemote().sendObject(message);
    }

    @OnMessage
    public void processMessageFromServer(GameMessage receivedMessage, Session session)
            throws IOException,
                EncodeException
    {
        for (String error : receivedMessage.lastTickErrors()) {
            System.out.println("Previous tick error: " + error);
        }
        System.out.println("Round " + receivedMessage.round() + ": Ticks until payout: " + receivedMessage.ticksUntilPayout());

        Command command = bot.getCommand(receivedMessage);
        BotCommandMessage botMessage = new BotCommandMessage(receivedMessage.tick(), command.getActions());

        session.getBasicRemote().sendObject(botMessage);
    }

    @OnClose
    public void onClose(CloseReason closeReason)
    {
        System.out.println("Closing the websocket for this reason: " + closeReason);
        latch.countDown();
    }
}