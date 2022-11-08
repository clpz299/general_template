package com.general.template.infrastructure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.util.Assert;


public class ResourceGrantedAuthority implements GrantedAuthority {
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final String resourceCode;

    @JsonCreator
    public ResourceGrantedAuthority(@JsonProperty("authority") String resourceCode) {
        Assert.hasText(resourceCode, "A granted authority textual representation is required");
        this.resourceCode = resourceCode;
    }

    @Override
    public String getAuthority() {
        return resourceCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof ResourceGrantedAuthority) {
            return resourceCode.equals(((ResourceGrantedAuthority) obj).resourceCode);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.resourceCode.hashCode();
    }

    @Override
    public String toString() {
        return this.resourceCode;
    }
}
