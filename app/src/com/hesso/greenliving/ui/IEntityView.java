package com.hesso.greenliving.ui;

import java.util.Observer;

public interface IEntityView<EntityType> extends Observer {
    public void setModel( EntityType entity );
}
