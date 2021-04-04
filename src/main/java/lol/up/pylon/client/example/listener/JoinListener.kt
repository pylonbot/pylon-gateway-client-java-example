package lol.up.pylon.client.example.listener

import bot.pylon.proto.discord.v1.event.EventEnvelope.HeaderData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import lol.up.pylon.gateway.client.entity.event.GuildMemberAddEvent
import lol.up.pylon.gateway.client.event.AbstractEventReceiver
import lol.up.pylon.gateway.client.extension.await
import lol.up.pylon.gateway.client.util.TimeUtil
import org.slf4j.LoggerFactory
import java.util.*

class JoinListener : AbstractEventReceiver<GuildMemberAddEvent>() {

    private val log = LoggerFactory.getLogger(JoinListener::class.java)

    // Kotlin example GrpcRequests
    override fun receive(headerData: HeaderData, event: GuildMemberAddEvent) {
        GlobalScope.launch {
            // timestamp when the event was received at the gateway
            val receivedAtMillis = TimeUtil.timestampToLong(headerData.receivedAt)

            // The member who joined
            val member = event.member

            // The guild "discord server" which has been joined
            // In kotlin we can use the extensions to await() the gateway response in a suspend coroutine
            val guild = event.guild.await()

            log.info("Recv {} - Member {} joined {}", Date(receivedAtMillis), member, guild)
        }
    }
}