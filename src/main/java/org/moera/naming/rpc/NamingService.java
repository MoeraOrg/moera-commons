package org.moera.naming.rpc;

import java.util.UUID;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;

@JsonRpcService("/moera-naming")
public interface NamingService {

    UUID put(
            @JsonRpcParam("name") String name,
            @JsonRpcParam("newGeneration") boolean newGeneration,
            @JsonRpcParam("updatingKey") String updatingKey,
            @JsonRpcParam("nodeUri") String nodeUri,
            @JsonRpcParam("signingKey") String signingKey,
            @JsonRpcParam("validFrom") Long validFrom,
            @JsonRpcParam("signature") String signature);

}
