package lol.up.pylon.client.example;

import lol.up.pylon.client.example.listener.JoinListener;
import lol.up.pylon.client.example.listener.LeaveListener;
import lol.up.pylon.client.example.listener.MessageListener;
import lol.up.pylon.gateway.client.GatewayGrpcClient;
import lol.up.pylon.gateway.client.entity.event.GuildMemberAddEvent;
import lol.up.pylon.gateway.client.entity.event.GuildMemberRemoveEvent;
import lol.up.pylon.gateway.client.entity.event.MessageCreateEvent;

public class ExampleBot {

    private final GatewayGrpcClient client;

    ExampleBot(final GatewayGrpcClient client) {
        this.client = client;
        registerListeners();
    }

    private void registerListeners() {
        client.registerReceiver(GuildMemberAddEvent.class, new JoinListener());
        client.registerReceiver(GuildMemberRemoveEvent.class, new LeaveListener());
        client.registerReceiver(MessageCreateEvent.class, new MessageListener());
    }
}
