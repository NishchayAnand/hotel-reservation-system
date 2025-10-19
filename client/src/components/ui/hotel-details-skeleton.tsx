
import { Skeleton } from "@/components/ui/skeleton";
import { Separator } from "@/components/ui/separator";

export default function HotelDetailsSkeleton() {
    return (
        <div className="flex flex-col space-y-3 mb-4">
            <Skeleton className="h-8 w-[400px]" />
            <Skeleton className="h-4 w-[250px]" />
            <Separator />     
            <div className="flex flex-wrap gap-2 my-5">
                 <Skeleton className="h-4 w-[100px]" />
                 <Skeleton className="h-4 w-[150px]" />
                 <Skeleton className="h-4 w-[100px]" />               
            </div>
            <Separator />          
            <Skeleton className="h-8 w-full" />
            <Skeleton className="h-[100px] w-full" />
        </div>
    );
}