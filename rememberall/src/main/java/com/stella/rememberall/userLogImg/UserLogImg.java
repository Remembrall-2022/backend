package com.stella.rememberall.userLogImg;

import com.stella.rememberall.placelog.PlaceLog;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Table(name = "user_img")
@Entity
public class UserLogImg {
    @Id
    @Column(name = "user_img_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int index;

    @Column(name = "img_url", nullable = false)
    private String fileKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_log_id")
    private PlaceLog placeLog;

    @Builder
    public UserLogImg(int index, String fileKey, PlaceLog placeLog){
        this.index = index;
        this.fileKey = fileKey;
        this.placeLog = placeLog;
    }

    public UserLogImg updateFileKey(String updatedFileKey) {
        this.fileKey = updatedFileKey;
        return this;
    }
}
