package org.moera.naming.rpc;

import java.util.List;
import java.util.UUID;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;

@JsonRpcService("/moera-naming")
public interface NamingService {

    UUID put(
            @JsonRpcParam("name") String name,
            @JsonRpcParam("generation") int generation,
            @JsonRpcParam("updatingKey") byte[] updatingKey,
            @JsonRpcParam("nodeUri") String nodeUri,
            @JsonRpcParam("signingKey") byte[] signingKey,
            @JsonRpcParam("validFrom") Long validFrom,
            @JsonRpcParam("previousDigest") byte[] previousDigest,
            @JsonRpcParam("signature") byte[] signature);

    OperationStatusInfo getStatus(@JsonRpcParam("operationId") UUID operationId);

    RegisteredNameInfo getCurrent(@JsonRpcParam("name") String name, @JsonRpcParam("generation") int generation);

    @Deprecated
    RegisteredNameInfo getCurrentForLatest(@JsonRpcParam("name") String name);

    RegisteredNameInfo getPast(@JsonRpcParam("name") String name, @JsonRpcParam("generation") int generation,
                               @JsonRpcParam("at") long at);

    @Deprecated
    RegisteredNameInfo getPastForLatest(@JsonRpcParam("name") String name, @JsonRpcParam("at") long at);

    @Deprecated
    boolean isFree(@JsonRpcParam("name") String name);

    boolean isFree(@JsonRpcParam("name") String name, @JsonRpcParam("generation") int generation);

    RegisteredNameInfo getSimilar(@JsonRpcParam("name") String name);

    List<RegisteredNameInfo> getAll(@JsonRpcParam("at") long at, @JsonRpcParam("page") int page,
                                    @JsonRpcParam("size") int size);

}
