package lol.up.pylon.client.example.listener;

import bot.pylon.proto.discord.v1.event.EventEnvelope;
import lol.up.pylon.gateway.client.entity.Channel;
import lol.up.pylon.gateway.client.entity.Guild;
import lol.up.pylon.gateway.client.entity.Member;
import lol.up.pylon.gateway.client.entity.Message;
import lol.up.pylon.gateway.client.entity.event.MessageCreateEvent;
import lol.up.pylon.gateway.client.event.AbstractEventReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageListener extends AbstractEventReceiver<MessageCreateEvent> {

    private final Logger log = LoggerFactory.getLogger(MessageListener.class);

    // Java example complex GrpcRequests
    @Override
    protected void receive(EventEnvelope.HeaderData headerData, MessageCreateEvent event) {
        // getGuild() returns a GrpcRequest, we transform it with another GrpcRequest (getChannel())
        // transformWith takes another GrpcRequest and uses a BiFunction <A,B,Result> which returns
        // a new GrpcRequest<Result>
        // The method-reference Tuple::new expands to (guild, channel) -> new Tuple<>(guild, channel)
        event.getGuild().transformWith(event.getChannel(), Tuple::new)
                // on the new GrpcRequest with our Tuple we do a flatTransform
                // flatTransform's main goal is to avoid GrpcRequest<GrpcRequest<Something>>
                // instead it flattens the second GrpcRequest into the parent one.
                // In this case getOwner() returns another GrpcRequest which we simply transform into a Triple
                // Simplified we see here flatTransform(something -> GrpcRequest<Triple>)
                // Yet a better example for flatTransform is getting a Guilds self member (bot as member) like
                // event.getGuild().flatTransform(Guild::getSelfMember).queue(member -> {});
                .flatTransform(tuple ->
                        tuple.first.getOwner().transform(owner -> new Triple<>(tuple.first, tuple.second, owner)))
                // We await the result of our GrpcRequest with an asynchronous callback using the queue() function
                .queue(triple -> {
                    final Guild guild = triple.first;
                    final Channel channel = triple.second;
                    final Member owner = triple.third;
                    final Message message = event.getMessage();

                    log.info("Received a message in guild {} owned by {}. Channel: {}, Message: {}",
                            guild, owner, channel, message);
                });
    }

    private static final class Tuple<A, B> {

        private A first;
        private B second;

        public Tuple(A first, B second) {
            this.first = first;
            this.second = second;
        }
    }

    private static final class Triple<A, B, C> {

        private A first;
        private B second;
        private C third;

        public Triple(A first, B second, final C third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }
    }
}
