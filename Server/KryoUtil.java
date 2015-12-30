package Server;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;

import NetObjects.ClientActions;
import NetObjects.LoginRequest;
import NetObjects.LoginResponse;
import NetObjects.RegisterRequest;
import NetObjects.RegisterResponse;
import NetObjects.Synch;

public class KryoUtil {

	public static final int TCP_PORT = 55223;
	public static final int UDP_PORT = 55224;

	public static void registerServerClasses(Server server) {
		register(server.getKryo());
	}

	public static void registerClientClass(Client client) {
		register(client.getKryo());
	}

	private static void register(Kryo kryo) {
		//kryo.register(TestObject.class);
		kryo.register(int.class);
		kryo.register(String.class);

		// network messages
		kryo.register(RegisterResponse.class);
		kryo.register(RegisterRequest.class);
		kryo.register(LoginResponse.class);
		kryo.register(LoginRequest.class);
		kryo.register(ClientActions.class);
		kryo.register(Synch.class);
	}
}
