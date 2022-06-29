package cgi.hacker.vaillant.rien.d.impossible.Hackatton.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailDto {

    private String from, to, copy, subject, date, inReplyTo;
    private int id;
}
