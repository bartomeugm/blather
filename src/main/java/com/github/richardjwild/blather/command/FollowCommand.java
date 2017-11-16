package com.github.richardjwild.blather.command;

import com.github.richardjwild.blather.datatransfer.User;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

public class FollowCommand implements Command {

    private final User follower;
    private final User subject;

    public FollowCommand(User follower, User subject) {
        this.follower = follower;
        this.subject = subject;
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean equals(Object o) {
        return reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }
}
