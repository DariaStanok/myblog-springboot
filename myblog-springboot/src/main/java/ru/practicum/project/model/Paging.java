package ru.practicum.project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Paging {

	private int pageSize;
    private int pageNumber;
    private boolean hasNext;
    
    public boolean hasPrevious() {
        return pageNumber > 1;
    }
}
