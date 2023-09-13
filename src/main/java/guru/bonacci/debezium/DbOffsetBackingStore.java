package guru.bonacci.debezium;

import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.runtime.WorkerConfig;
import org.apache.kafka.connect.storage.MemoryOffsetBackingStore;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class DbOffsetBackingStore extends MemoryOffsetBackingStore {

	Connection conn;

	@Override
	public void configure(WorkerConfig config) {
		super.configure(config);

		String connectionUrl = "jdbc:mysql://localhost:3305/customerdb2";

		try {
			conn = DriverManager.getConnection(connectionUrl, "root", "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void start() {
		super.start();
		log.info("Starting DbOffsetBackingStore with file {}", "foo");
		load();
	}

	@Override
	public synchronized void stop() {
		super.stop();
		// Nothing to do since this doesn't maintain any outstanding connections/data
		log.info("Stopped DbOffsetBackingStore");
	}

	private void load() {
		data = new HashMap<>();

    final String sqlLoad = "SELECT * FROM debezium_offsets";

		try {
			var ps = conn.prepareStatement(sqlLoad);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				ByteBuffer key = ByteBuffer.wrap(rs.getString("ke").getBytes());
				ByteBuffer value = ByteBuffer.wrap(rs.getBytes("va"));
				data.put(key, value);
			}

		} catch (SQLException e) {
			throw new ConnectException(e);
		}
	}

	@Override
	protected void save() {
		try {
			final String sqlDel = "DELETE FROM debezium_offsets";
			conn.prepareStatement(sqlDel).executeUpdate();

			final String sqlSave = "INSERT INTO debezium_offsets (ke, va) VALUES (?, ?)";
	
	    for (Map.Entry<ByteBuffer, ByteBuffer> mapEntry : data.entrySet()) {
				byte[] key = (mapEntry.getKey() != null) ? mapEntry.getKey().array() : null;
	    	log.info("saving " + new String(key));
				byte[] value = (mapEntry.getValue() != null) ? mapEntry.getValue().array() : null;

				var ps = conn.prepareStatement(sqlSave);
				ps.setString(1, new String(key));
				ps.setBytes(2, value);
				ps.executeUpdate();
			}
    
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ConnectException(e);
		}
	}
}