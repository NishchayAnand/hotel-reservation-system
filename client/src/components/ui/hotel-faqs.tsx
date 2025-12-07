
import {Accordion, AccordionContent, AccordionItem, AccordionTrigger} from "@/components/ui/accordion"

export default function HotelFaqs() {
    return (
        <article id="faqs-container"className="flex flex-col gap-4 mb-4">
            <h2 className="text-md font-semibold">FAQs</h2>
            <Accordion
                type="single"
                collapsible
                className="w-full"
                defaultValue="item-1"
            >
                <AccordionItem value="item-1">
                    <AccordionTrigger>Rules and Regulations</AccordionTrigger>
                    <AccordionContent className="flex flex-col gap-4 text-balance">
                        <ul className="list-disc pl-5">
                            <li>
                                Guests are required to present a valid government-issued ID at the time of check-in. 
                                The primary guest must be at least 18 years old.
                            </li>
                            <li>
                                Smoking is strictly prohibited in all indoor areas. A designated smoking area is available outside the premises.
                            </li>
                            <li>
                                Pets are not allowed on the property. Service animals are permitted with prior notice.
                            </li>
                            <li>
                                Please respect the quiet hours between 10 PM and 7 AM to ensure a comfortable stay for all guests.
                            </li>
                        </ul>
                    </AccordionContent>
                </AccordionItem>

                <AccordionItem value="item-2">
                    <AccordionTrigger>Cancellation Policy</AccordionTrigger>
                    <AccordionContent className="flex flex-col gap-4">
                        <p>
                            Our flagship product combines cutting-edge technology with sleek
                            design. Built with premium materials, it offers unparalleled
                            performance and reliability.
                        </p>
                        <p>
                            Key features include advanced processing capabilities, and an
                            intuitive user interface designed for both beginners and experts.
                        </p>
                    </AccordionContent>
                </AccordionItem>

                <AccordionItem value="item-3">
                    <AccordionTrigger>Refund Policy</AccordionTrigger>
                    <AccordionContent className="flex flex-col gap-4">
                        <p>
                            We are committed to ensuring customer satisfaction with a transparent refund policy. 
                            If you are eligible for a refund, the amount will be processed back to your original payment method.
                        </p>
                        <p>
                            Refunds are typically processed within 7-10 business days after the request is approved. 
                            Please note that certain conditions may apply, and processing times may vary depending on your payment provider.
                        </p>
                    </AccordionContent>
                </AccordionItem>
            </Accordion>
        </article>
    );
}