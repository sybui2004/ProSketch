package app.model.Interface;

import app.model.typeEnum.NotificationType;
public interface IDrawingModelListener {
    void onDrawingModelChange(NotificationType notificationType);
}
