package utilities;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ListView;

public class ListViewFixed<T> extends ListView<T>
{
    private final BooleanProperty fillWidth = new SimpleBooleanProperty(this, "fillWidth");

    public final BooleanProperty fillWidthProperty()
    {
        return fillWidth;
    }

    public final boolean isFillWidth()
    {
        return fillWidth.get();
    }

    public final void setFillWidth(boolean fillWidth)
    {
        this.fillWidth.set(fillWidth);
    }

    @Override
    protected ListViewFixedSkin<T> createDefaultSkin()
    {
        return new ListViewFixedSkin<>(this);
    }
}
