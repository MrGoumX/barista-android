package gr.aueb.android.barista.core.annotations.factories;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;

import gr.aueb.android.barista.core.annotations.Permission;
import gr.aueb.android.barista.core.model.CommandDTO;
import gr.aueb.android.barista.core.model.PmGrantDTO;

public class PmGrantCommandFactory implements CommandFactory {

    @Override
    public Collection<CommandDTO> constructCommand(Annotation a) {
        String permission = ((Permission)a).type();

        PmGrantDTO grantCommand = new PmGrantDTO(null, permission);
        return Arrays.asList(grantCommand);
    }
}
