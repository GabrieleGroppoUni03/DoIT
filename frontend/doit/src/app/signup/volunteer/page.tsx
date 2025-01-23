"use client"

import {VolunteerForm} from "@/components/refactored/form/auth/VolunteerForm";
import FormHeader from "@/components/ui/FormHeader";
import {useBack} from "@/hooks/refactored/useBack";

export default function VolunteerSignUp() {
    const handleBack = useBack();

    return (
        <div>
            <div className="block md:hidden">
                <FormHeader
                    title="Registra un volontario"
                    subtitle="Aggiungi un nuovo volontario e aiuta più organizzazioni possibili"
                    onBack={handleBack}
                />
            </div>
            <VolunteerForm/>
        </div>
    );
}