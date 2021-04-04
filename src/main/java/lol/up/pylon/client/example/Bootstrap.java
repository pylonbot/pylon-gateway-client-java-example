package lol.up.pylon.client.example;

import lol.up.pylon.gateway.client.GatewayGrpcClient;
import lol.up.pylon.gateway.client.event.EventSuppliers;

public class Bootstrap {

    public static void main(final String[] args) {
        final GatewayGrpcClient client = GatewayGrpcClient.builder(Long.parseLong(env("DEFAULT_BOT_ID")))
                .setRouterHost(env("ROUTER_HOST"))
                .setRouterPort(Integer.parseInt(env("ROUTER_PORT")))
                .build();
        // TODO: Replace grpc server implementation with worker groups
        /*
        client.registerEventSupplier(EventSuppliers.grpcWorkerGroupSupplier(
                env("WORKER_GROUP_AUTH_TOKEN"),
                env("WORKER_GROUP_CONSUMER_GROUP"),
                env("WORKER_GROUP_CONSUMER_ID")
        ));
        */
        client.registerEventSupplier(EventSuppliers.grpcServerEventSupplier(Integer.parseInt("GRPC_SERVER")));
        new ExampleBot(client);
    }

    private static String env(final String env) {
        final String value = System.getenv(env);
        if (value == null) {
            throw new IllegalArgumentException("Env " + env + " must be provided");
        }
        return env;
    }
}
