package com.bervan.interviewapp.view;

import com.bervan.interviewapp.model.PersistableTableData;
import com.bervan.interviewapp.service.BaseService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractTableView<T extends PersistableTableData> extends PageLayout {
    protected final BaseService<T> service;
    protected final List<T> data;
    protected final Grid<T> grid;


    public AbstractTableView(String currentRouteName, @Autowired BaseService<T> service, String pageName) {
        super(currentRouteName);
        this.service = service;

        data = this.service.load();

        //view
        H3 header = new H3(pageName);
        grid = getGrid();
        grid.setItems(data);
        grid.addItemClickListener(this::openEditDialog);
        grid.getColumns().forEach(column -> column.setClassNameGenerator(item -> "top-aligned-cell"));

        TextField searchField = getFilter();

        Button addButton = new Button("Add New Element", e -> openAddDialog());
        add(header, searchField, grid, addButton);

    }

    protected void filterTable(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            grid.setItems(data);
        } else {
            List<T> collect = data.stream()
                    .filter(q -> q.getName().toLowerCase().contains(filterText.toLowerCase()))
                    .collect(Collectors.toList());
            grid.setItems(collect);
        }
    }

    protected TextField getFilter() {
        TextField searchField = new TextField("Filter table...");
        searchField.setWidth("100%");
        searchField.addValueChangeListener(e -> filterTable(e.getValue()));
        return searchField;
    }

    protected HorizontalLayout getDialogTopBarLayout(Dialog dialog) {
        Button closeButton = new Button(new Icon(VaadinIcon.CLOSE));
        closeButton.addClickListener(e -> dialog.close());
        HorizontalLayout headerLayout = new HorizontalLayout(closeButton);
        headerLayout.setWidthFull();
        headerLayout.setJustifyContentMode(JustifyContentMode.END);
        return headerLayout;
    }

    protected abstract Grid<T> getGrid();

    protected abstract void openEditDialog(ItemClickEvent<T> event);

    protected Span formatTextComponent(String text) {
        if (text == null) {
            return new Span("");
        }
        Span span = new Span();
        span.getElement().setProperty("innerHTML", text.replace("\n", "<br>").replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;"));
        return span;
    }

    protected abstract void openAddDialog();
}
