package com.nli.probation.converter;

import com.nli.probation.customexception.NoSuchFieldOfClassException;
import com.nli.probation.model.RequestPaginationModel;
import com.nli.probation.model.ResourceModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static com.nli.probation.utils.ValidatorUtils.checkExistFieldOfClass;

/**
 * Class for supporting pagination
 * @param <M> Type of model
 * @param <E> Type of Entity
 */
public class PaginationConverter<M, E> {

    /**
     * Create pageable for sort
     * @param paginationModel
     * @param defaultSortBy
     * @return
     */
    public Pageable convertToPageable(RequestPaginationModel paginationModel, String defaultSortBy, Class<E> classType) {
        //Define sort by field for paging
        String sortBy = defaultSortBy;
        if(paginationModel.getSortBy() != null) {
            if(!checkExistFieldOfClass(classType, paginationModel.getSortBy())) {
                throw new NoSuchFieldOfClassException("Can not define sortBy");
            }
            sortBy = paginationModel.getSortBy();
        }

        //Build Pageable
        Pageable pageable;
        if (paginationModel.getSortType() != null && paginationModel.getSortType().equals("dsc")) {
            pageable = PageRequest.of(paginationModel.getIndex(), paginationModel.getLimit(), Sort.by(sortBy).descending());
        } else {
            pageable = PageRequest.of(paginationModel.getIndex(), paginationModel.getLimit(), Sort.by(sortBy).ascending());
        }
        return pageable;
    }

    /**
     * Build pagination
     * @param pagination
     * @param page
     * @param resource
     * @return
     */
    public ResourceModel<M> buildPagination(RequestPaginationModel pagination, Page<E> page, ResourceModel<M> resource) {
        resource.setTotalPage(page.getTotalPages());
        resource.setTotalResult((int) page.getTotalElements());
        resource.setIndex(pagination.getIndex());
        resource.setLimit(pagination.getLimit());
        return resource;
    }
}
