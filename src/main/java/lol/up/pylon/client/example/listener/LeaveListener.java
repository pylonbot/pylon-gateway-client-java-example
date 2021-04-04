package lol.up.pylon.client.example.listener;

import bot.pylon.proto.discord.v1.event.EventEnvelope;
import lol.up.pylon.gateway.client.entity.Member;
import lol.up.pylon.gateway.client.entity.event.GuildMemberRemoveEvent;
import lol.up.pylon.gateway.client.event.AbstractEventReceiver;
import lol.up.pylon.gateway.client.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class LeaveListener extends AbstractEventReceiver<GuildMemberRemoveEvent> {

    private final Logger log = LoggerFactory.getLogger(LeaveListener.class);

    // Java example GrpcRequests
    @Override
    protected void receive(EventEnvelope.HeaderData headerData, GuildMemberRemoveEvent event) {

        // timestamp when the event was received at the gateway
        final long receivedAtMillis = TimeUtil.timestampToLong(headerData.getReceivedAt());

        // The member who joined
        final Member member = event.getMember();

        // The guild "discord server" which has been joined
        // This is a grpc request, it can either be .complete()'d or queue()'d
        // A complete() will block the current thread until the response is received
        // A queue() takes a callback which runs asynchronously as the response is received
        event.getGuild().queue(guild ->
                log.info("Recv {} - Member {} left {}", new Date(receivedAtMillis), member, guild));
    }
}
