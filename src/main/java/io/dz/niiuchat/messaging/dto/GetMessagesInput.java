package io.dz.niiuchat.messaging.dto;

import io.dz.niiuchat.common.PageInfo;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class GetMessagesInput {

    @Min(1)
    @Max(20)
    private Integer limit;

    @Min(0)
    private Integer offset;

    public PageInfo toPageInfo() {
        var pageInfo = new PageInfo();
        pageInfo.setOffset(offset);
        pageInfo.setLimit(limit);

        return pageInfo;
    }

}
