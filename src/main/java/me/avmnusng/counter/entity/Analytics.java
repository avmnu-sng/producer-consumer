package me.avmnusng.counter.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * The `analytics` table persists the event when the counter value is equal to either the lower limit
 * or the upper limit. It persists the following attributes:
 * - id is the primary key for the record
 * - worker_type is the enum(producer, consumer)
 * - worker_id is the unique id associated with the worker
 * - value is the current value, which will be either the lower limit or upper limit
 * - create_at is the time of record creation
 */
@Entity
@Table(name = "analytics")
public class Analytics {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id")
    private long id;

    @Column(name = "worker_type", columnDefinition = "enum")
    private String workerType;

    @Column(name = "worker_id", unique = true, length = 36)
    private String workerId;

    @Column(name = "value")
    private Integer value;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    protected Analytics() {
    }

    public Analytics(String workerType, String workerName, int value) {
        this.workerType = workerType;
        this.workerId = workerName.split(workerType)[1].substring(1);
        this.value = value;
        this.createdAt = new Date();
    }
}
