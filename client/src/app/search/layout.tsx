
import { SearchNavbar } from "@/components/ui/search-navbar";

export default function SearchLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <>
        <SearchNavbar />
        {children}
    </>
  );
}
