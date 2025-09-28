import Link from "next/link";
import { Button } from "@/components/ui/button";

export function Navbar() {
    return (
        <nav className="fixed left-0 top-0 w-full z-100 h-[4rem] bg-white flex items-center justify-between px-8 border-b">

            {/* Logo */}
            <Link href="/" className="text-lg font-bold">NIVARA</Link>

            {/* Sign In Button */}
            <Button size="sm" asChild>
                <Link href="/signin"> Sign In</Link>
            </Button>
            
        </nav>
    );
}