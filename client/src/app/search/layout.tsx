
import { Navbar2 } from "@/components/ui/navbar2";

export default function SearchLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <>
        <Navbar2 />
        {children}
    </>
  );
}
