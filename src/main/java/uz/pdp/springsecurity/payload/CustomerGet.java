package uz.pdp.springsecurity.payload;

import java.util.UUID;

public record CustomerGet(UUID id, String firstName, String lastName) {
}
