package pl.mjasion.httpapi.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class AccessHistory {
    @Id
    String id;

    String ip

    Date accessDate

    Site site
}