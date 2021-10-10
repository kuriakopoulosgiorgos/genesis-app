package gr.uth.dto;

import java.util.List;

public class Pageable<T> {
    public List<T> data;
    public Long totalCount;
}
