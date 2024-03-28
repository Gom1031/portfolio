package com.goormpj.decimal.ide.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FolderRequestDTO {

    private String folderName;
    private Long parentId;
    private String fileName;

}
