library ieee;
use ieee.std_logic_1164.all;
 
entity VL_AND_tb is
end VL_AND_tb;
 
architecture strukturna of VL_AND_tb is
  signal a: std_logic_vector(2 downto 0);
  signal f: std_logic;
begin

  uut: entity work.VL_AND generic map (n=>3, delay=>20 ns) port map (a,f);
 
  process
  begin
    a(0) <= '0';
    a(1) <= '0';
    a(2) <= '0';
    wait for 100 ns;
 
    a(0) <= '0';
    a(1) <= '0';
    a(2) <= '1';
    wait for 100 ns;

    a(0) <= '0';
    a(1) <= '1';
    a(2) <= '0';
    wait for 100 ns;
 
    a(0) <= '0';
    a(1) <= '1';
    a(2) <= '1';
    wait for 100 ns;
 
    a(0) <= '1';
    a(1) <= '0';
    a(2) <= '0';
    wait for 100 ns;
 
    a(0) <= '1';
    a(1) <= '0';
    a(2) <= '1';
    wait for 100 ns;

    a(0) <= '1';
    a(1) <= '1';
    a(2) <= '0';
    wait for 100 ns;
 
    a(0) <= '1';
    a(1) <= '1';
    a(2) <= '1';
    wait for 100 ns;

    wait;
  end process;
   
end strukturna;
 
 

