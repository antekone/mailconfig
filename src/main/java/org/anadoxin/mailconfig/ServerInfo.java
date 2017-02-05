package org.anadoxin.mailconfig;

import org.anadoxin.mailconfig.boot.Log;
import lombok.*;

public class ServerInfo {
    @Getter @Setter String hostName;
    @Getter @Setter String protocol;
    @Getter @Setter boolean wantSSL;
}
