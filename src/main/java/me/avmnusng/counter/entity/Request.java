package me.avmnusng.counter.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * The `requests` table persists the requests for creating producer and consumer threads. It persists the following
 * attributes:
 * - id is the primary key for the record
 * - worker_type is the enum(producer, consumer)
 * - worker_id is the unique id associated with the worker
 * - create_at is the time of record creation
 */
@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id")
    private long id;

    @Column(name = "worker_type", columnDefinition = "enum")
    private String workerType;

    @Column(name = "worker_id", unique = true, length = 36)
    private String workerId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    protected Request() {
    }

    public Request(String workerType, String workerName) {
        this.workerType = workerType;
        this.workerId = workerName.split(workerType)[1].substring(1);
        this.createdAt = new Date();
    }
}
