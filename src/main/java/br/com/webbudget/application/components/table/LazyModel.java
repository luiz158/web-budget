/*
 * Copyright (C) 2015 Arthur Gregorio, AG.Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.com.webbudget.application.components.table;

import br.com.webbudget.domain.entities.PersistentEntity;
import static com.google.common.base.Preconditions.checkNotNull;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

/**
 * LazyDataModel generico para uso nas datatables do sistema. Como ele podemos
 * definir a carga de um datatable on-demand
 *
 * @param <T> o tipo deste model
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 2.1.0, 05/09/2015
 */
public class LazyModel<T extends PersistentEntity> extends LazyDataModel<T> {

    private final LazyDataProvider<T> provider;
    
    /**
     * Constructor...
     *
     * @param provider the dataprovider for this model
     */
    public LazyModel(LazyDataProvider<T> provider) {
        this.provider = checkNotNull(provider);
    }

    /**
     * {@inheritDoc }
     *
     * @param first
     * @param pageSize
     * @param multiSortMeta
     * @param filters
     * @return
     */
    @Override
    public List<T> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
        return this.countAndReturn(this.provider.load(first, pageSize, multiSortMeta));
    }

    /**
     * {@inheritDoc }
     *
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @param filters
     * @return
     */
    @Override
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        return this.countAndReturn(this.provider.load(first, pageSize, sortField, sortOrder));
    }

    /**
     * {@inheritDoc }
     *
     * @param object
     * @return
     */
    @Override
    public Object getRowKey(T object) {
        return object.getId();
    }

    /**
     * {@inheritDoc }
     *
     * @param rowKey
     * @return
     */
    @Override
    public T getRowData(String rowKey) {
        return this.getWrappedData().stream()
                .filter(object -> object.getId().equals(Long.parseLong(rowKey)))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Count the quantity of rows and return the data
     * 
     * @param data the data to be used on the datatable
     * @return the data to be used on the datatable
     */
    private List<T> countAndReturn(List<T> data) {
        this.setRowCount(data.size());
        return data;
    }
}