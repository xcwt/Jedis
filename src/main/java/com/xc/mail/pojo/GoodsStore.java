package com.xc.mail.pojo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity(name = "goods_store")
@Table(name = "goods_store")
@Data
public class GoodsStore implements Serializable {

    private static final long serialVersionUID = 1L;


    @Id
    private String code;

    @Column(name="store")
    private int store;

    @Column(name = "updatetime")
    private String updatetime;
}
