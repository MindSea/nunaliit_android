package ca.carleton.gcrc.n2android_mobile1.connection;

import com.couchbase.lite.Database;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Mapper;

import java.util.Map;

import ca.carleton.gcrc.n2android_mobile1.couchbase.CouchbaseDb;
import ca.carleton.gcrc.n2android_mobile1.couchbase.CouchbaseView;

/**
 * Created by jpfiset on 3/25/16.
 */
public class RevisionDb extends CouchbaseDb {

    public static final CouchbaseView viewConnectionsById = new CouchbaseView(){
        @Override
        public String getName() { return "connections-by-id"; }

        @Override
        public String getVersion() { return "1"; }

        @Override
        public Mapper getMapper() {
            return mapper;
        }

        private Mapper mapper = new Mapper(){
            @Override
            public void map(Map<String, Object> document, Emitter emitter) {
                Object connInfoObj = document.get("mobile_connection");
                if (null != connInfoObj && connInfoObj instanceof Map) {
                    Object idObj = document.get("_id");
                    if (null != idObj && idObj instanceof String) {
                        String id = (String) idObj;
                        emitter.emit(id, null);
                    }
                }
            }
        };
    };

    public RevisionDb(Database database) throws Exception {
        super(database);

        installView(viewConnectionsById);
    }
}
