package com.hexanome16.client.requests;

import com.almasb.fxgl.core.asset.AssetType;
import com.almasb.fxgl.core.collection.PropertyMap;
import com.almasb.fxgl.dsl.FXGL;
import lombok.Getter;

/**
 * Enum used to indicate the destination for a request.
 */
public enum RequestDest {
  LS("LS"),
  SERVER("SERVER");

  @Getter
  private final String url;

  RequestDest(String kind) {
    PropertyMap lsProperties = FXGL.getAssetLoader().load(
        AssetType.PROPERTY_MAP,
        "properties/lobby-service-" + System.getenv("PROFILE_ID") + ".properties"
    );
    PropertyMap serverProperties = FXGL.getAssetLoader().load(
        AssetType.PROPERTY_MAP,
        "properties/server-" + System.getenv("PROFILE_ID") + ".properties"
    );
    switch (kind) {
      case "LS" -> this.url = lsProperties.getString("server.protocol") + "://"
          + lsProperties.getString("server.host") + ":"
          + lsProperties.getInt("server.port");
      case "SERVER" -> this.url = serverProperties.getString("server.protocol") + "://"
          + serverProperties.getString("server.host") + ":"
          + serverProperties.getInt("server.port");
      default -> throw new IllegalArgumentException("Invalid RequestKind: " + kind);
    }
  }
}
